# GetServerStatus

`GetServerStatus` は、Minecraft サーバーの状態を Discord から確認できる Spigot/Paper 向けプラグインです。  
Discord のメインチャンネルで `getstatus` と送信すると、サーバー情報を Embed 形式で返します。

## 主な機能

- 現在のオンライン人数
- オンライン中プレイヤー名一覧
- TPS
- MSPT
- メモリ使用量
- CPU 使用率

## 動作条件

- Minecraft 1.18 以降
- `DiscordSRV`
- `spark`

## 使い方

1. サーバーに本プラグインを導入します。
2. `DiscordSRV` と `spark` を導入して起動します。
3. DiscordSRV のメインチャンネルで `getstatus` と送信します。
4. Bot がサーバー状態を Embed で返信します。

## 補足

- このプラグインは DiscordSRV のメインチャンネルへのメッセージのみを監視します。
- サーバー負荷の目安として、TPS に応じて Embed 色が変化します。
